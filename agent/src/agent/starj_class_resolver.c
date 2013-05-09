#include "starj_class_resolver.h"
#include "starj_io.h"
#include "starj_util.h"
#include "../data/starj_hash_map.h"
#include "../data/starj_data_util.h"
#include "../cfreader/cfreader_file.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

static sjdt_hash_map class_names_to_record;
static sjdt_hash_set processed_archives;

struct class_record {
    enum {kind_class, kind_jar} kind;
    char *path;
    unz_file_pos file_pos;
};

void release_class_record(sjdt_key key) {
    struct class_record *rec = (struct class_record *) key;
    if (rec != NULL) {
        if ((rec->kind == kind_class) && (rec->path != NULL)) {
            free(rec->path);
            rec->path = NULL;
        }

        free(rec);
    }
}

int remove_cp_element(size_t index) {
    size_t i;
    size_t j;
    char **new_class_path;
    
    SJAM_START_GLOBAL_ACCESS();
    if (index >= sjav_class_path_len) {
        SJAM_END_GLOBAL_ACCESS();
        return STARJ_ERR_INDEX_OUT_OF_BOUNDS;
    }

    new_class_path = SJAM_NEW_ARRAY(char *, sjav_class_path_len - 1);
    if (new_class_path == NULL) {
        SJAM_END_GLOBAL_ACCESS();
        return STARJ_ERR_MALLOC;
    }

    for (i = 0, j = 0; i < sjav_class_path_len; i++) {
        if (i != index) {
            new_class_path[j] = sjav_class_path[i];
            j++;
        }
    }

    sjav_class_path = new_class_path;
    sjav_class_path_len -= 1;
    SJAM_END_GLOBAL_ACCESS();

    return STARJ_OK;
}

int parse_class_from_record(struct class_record *record, const char *class_name, cfrt_classfile *c) {
    cfrt_file *f;
    int result;
    char *buff = NULL;
    size_t buff_len = STARJ_BUFF_SIZE;
    
    if (record == NULL) {
        return STARJ_ERR_NULL_PTR;
    }

    switch (record->kind) {
        case kind_class:
            f = cfrf_fopen_regular(record->path, "rb");
            break;
        case kind_jar:
            f = cfrf_fopen_archive_location(record->path, &(record->file_pos));
            break;
        default:
            sjaf_error("Unsupported class record kind: %d", record->kind);
            return STARJ_ERR_RECORD_KIND;       
    }

    if (f == NULL) {
        sjaf_error("Failed to open file \"%s\"", record->path);
        return STARJ_ERR_FILE_OPEN;
    }

    result = cfrf_parse_class(c, f);
    cfrf_fclose(f);
    if (result != CFREADER_OK) {
        sjaf_error("Failed to parse class %s from \"%s\"", class_name, record->path);
        return STARJ_ERR_CLASS_PARSE;
    }


    while (true) {
        if (buff != NULL) {
            free(buff);
        }
        buff = SJAM_NEW_ARRAY(char, buff_len);
        if (buff == NULL) {
            sjaf_error("Failed to allocate class name buffer");
            return STARJ_ERR_MALLOC;
        }

        result = cfrf_get_class_name(c, buff, buff_len);
        if (result == CFREADER_ERR_BUFFER_SIZE) {
            /* We ran out of space. Double the size of the array and
             * try again. */
            buff_len *= 2;
        } else {
            break;
        }
    }

    if (!strcmp(class_name, buff)) {
        result = STARJ_OK;
    } else {
        result = STARJ_ERR_CLASS_NAME;
    }
    
    free(buff);
    return result;
}

int process_jar_file(char *jar_name) {
    struct stat jar_stat;
    int result = STARJ_OK;
    bool file_opened = false;
    unzFile unzip_file = NULL;
    unz_global_info global_info;
    uLong l;
    char *s = NULL;
    struct class_record *class_rec = NULL;

    sjdf_hash_set_add(&processed_archives, jar_name);

    sjaf_debug("Processing archive \"%s\"", jar_name);
    
    if (jar_name == NULL) {
        return STARJ_ERR_NULL_PTR;
    }

    if ((stat(jar_name, &jar_stat) < 0) || !S_ISREG(jar_stat.st_mode)) {
        return STARJ_ERR_FILE_NOT_REG;
    }

    unzip_file = unzOpen(jar_name);

    if (unzip_file == NULL) {
        sjaf_warning("Failed to open class path entry %s", jar_name);
        result = STARJ_ERR_FILE_OPEN;
        goto cleanup;
    }

    if (unzGetGlobalInfo(unzip_file, &global_info) != UNZ_OK) {
        sjaf_warning("Failed to get global info");
        result = STARJ_ERR_ZIP_GLOBAL_INFO;
        goto cleanup;
    }

    for (l = 0; l < global_info.number_entry; l++) {
        char filename_inzip[256];
        unz_file_info file_info;
        
        if (unzGetCurrentFileInfo(unzip_file, &file_info, filename_inzip,
                    sizeof(filename_inzip), NULL, 0, NULL, 0) != UNZ_OK) {
            sjaf_warning("Failed to get file info");
            result = STARJ_ERR_ZIP_FILE_INFO;
            goto cleanup;
        }

        file_opened = true;

        if (sjaf_ends_with(filename_inzip, ".class")) {
            size_t name_len;

            class_rec = SJAM_NEW(struct class_record);
            if (class_rec == NULL) {
                result = STARJ_ERR_MALLOC;
                sjaf_warning("Failed to allocate class record");
                goto cleanup;
            }

            class_rec->kind = kind_jar;
            class_rec->path = jar_name;

            name_len = strlen(filename_inzip);
            s = SJAM_NEW_ARRAY(char, name_len - 5); /* name_len - strlen(".class") + 1 */
            if (s == NULL) {
                result = STARJ_ERR_MALLOC;
                sjaf_warning("Failed to allocate class name array");
                goto cleanup;
            }
            strncpy(s, filename_inzip, name_len - 6); /* name_len - strlen(".class") */
            sjaf_replace(s, '/', '.');
            
            if (unzGetFilePos(unzip_file, &(class_rec->file_pos)) != UNZ_OK) {
                result = STARJ_ERR_ZIP_FILE_POS;
                sjaf_warning("Failed to get file position");
                goto cleanup;
            }

            result = sjdf_hash_map_put(&class_names_to_record, s, class_rec);
            if (result != STARJ_DATA_OK) {
                sjaf_warning("Failed to store class record in map");
                goto cleanup;
            }

            s = NULL;
            class_rec = NULL;

        }

        unzCloseCurrentFile(unzip_file);
        file_opened = false;
        if ((l != global_info.number_entry - 1) && (unzGoToNextFile(unzip_file) != UNZ_OK)) {
            result = STARJ_ERR_ZIP_NEXT_FILE;
            sjaf_warning("Could not fetch next file from jar");
            goto cleanup;
        }

        if (l != global_info.number_entry - 1) {
            file_opened = true;
        }
    }
        
    result = STARJ_OK;

cleanup:
    if (s != NULL) {
        free(s);
    }

    if (class_rec != NULL) {
        free(class_rec);
    }
    
    if (file_opened) {
        unzCloseCurrentFile(unzip_file);
    }
    
    if (unzip_file != NULL) {
        unzClose(unzip_file);
    }

    return result;
}

int sjaf_class_resolver_init() {
    sjdf_hash_map_init(&class_names_to_record, 512, 0.75, sjdf_string_hash,
            sjdf_string_key_comp, NULL, sjdf_string_key_release,
            NULL, NULL, release_class_record);
    sjdf_hash_set_init(&processed_archives, 32, 0.75, sjdf_string_hash,
            sjdf_string_key_comp, sjdf_string_key_clone, sjdf_string_key_release);
    return STARJ_OK;
}

int sjaf_class_resolver_release() {
    sjdf_hash_set_release(&processed_archives);
    sjdf_hash_map_release(&class_names_to_record);

    return STARJ_OK;
}

int sjaf_resolve_class(const char *class_name, cfrt_classfile *c) {
    int i;
    struct class_record *record;
    int result;
    
    if ((class_name == NULL) || (c == NULL)) {
        return STARJ_ERR_NULL_PTR;
    }

    SJAM_START_CLASS_RESOLVER_ACCESS();
    if (sjdf_hash_map_get(&class_names_to_record, (sjdt_key) class_name, (sjdt_value *) &record) == STARJ_OK) {
        /* Found it */
        SJAM_END_CLASS_RESOLVER_ACCESS();
        return parse_class_from_record(record, class_name, c);
    }
    
    for (i = 0; i < sjav_class_path_len; ) {
        char *path = sjav_class_path[i];
        struct stat path_stat;

        if (path == NULL) {
            sjaf_warning("Class path contains a NULL element");
            if ((result = remove_cp_element(i)) != STARJ_OK) {
                sjaf_warning("Failed to remove bad class path entry");
                goto next_iteration;
            }
            continue;
        }

        if (stat(path, &path_stat) < 0) {
            if (errno == ENOENT) {
                sjaf_warning("Class path contains a non-existant element: \"%s\"", path);

                if ((result = remove_cp_element(i)) != STARJ_OK) {
                    sjaf_warning("Failed to remove bad class path entry");
                    goto next_iteration;
                }
                continue;
            }

            sjaf_warning("Failed to obtain stat for class path entry \"%s\"", path);
            goto next_iteration;
        }

        if (S_ISDIR(path_stat.st_mode)) {
            /* If the class file is found is this directory, then we can
             * guess its location based on the fully qualified class name. */
            char *th_name;
            size_t class_name_len = strlen(class_name);
            size_t path_len = strlen(path);
            bool path_has_slash = (path[path_len - 1] == '/');
            size_t th_name_len = path_len + class_name_len + 6 + (path_has_slash ? 0 : 1) + 1;

            th_name = SJAM_NEW_ARRAY(char, th_name_len);
            if (th_name == NULL) {
                sjaf_error("Failed to allocate memory for the theoretical class name");
                goto next_iteration;
            }
            
            strcpy(th_name, path);
            if (!path_has_slash) {
                th_name[path_len] = '/';
                th_name[path_len + 1] = '\0';
            }
            strcat(th_name, class_name);
            sjaf_replace(th_name + path_len, '.', '/');
            strcat(th_name, ".class");
            
            if (stat(th_name, &path_stat) < 0) {
                free(th_name);
                goto next_iteration;
            }

            if (S_ISREG(path_stat.st_mode)) {
                struct class_record th_rec;
                /* Potential hit */

                th_rec.path = th_name;
                th_rec.kind = kind_class;

                if (parse_class_from_record(&th_rec, class_name, c) == STARJ_OK) {
                    struct class_record *new_rec = SJAM_NEW(struct class_record);
                    if (new_rec != NULL) {
                        char *new_class_name;
                        new_rec->path = th_name;
                        new_rec->kind = kind_class;
                        /* Make a copy of the string for storage, since
                         * we can't trust the original string to stay around */
                        new_class_name = strdup(class_name); 
                        if (new_class_name == NULL) {
                            return STARJ_ERR_MALLOC;
                        }
                        sjdf_hash_map_put(&class_names_to_record, (sjdt_key) new_class_name, new_rec);
                    } else {
                        free(th_name);
                    }
                    SJAM_END_CLASS_RESOLVER_ACCESS();
                    return STARJ_OK;
                }
            }

            free(th_name);
        } else if (S_ISREG(path_stat.st_mode) && (sjaf_ends_with(path, ".jar")
                || sjaf_ends_with(path, ".zip"))) {
            if (sjdf_hash_set_contains(&processed_archives, path) != STARJ_DATA_OK) {
                process_jar_file(path);
            }
            if (sjdf_hash_map_get(&class_names_to_record, (sjdt_key) class_name, (sjdt_value *) &record) == STARJ_OK) {
                /* This jar/zip file contained the desired class */
                if (parse_class_from_record(record, class_name, c) == STARJ_OK) {
                    SJAM_END_CLASS_RESOLVER_ACCESS();
                    return STARJ_OK;
                }
            }
        } else {
            sjaf_warning("Class path contains an entry which is neither a directory");
            sjaf_warning("    nor a regular file: \"%s\"", path);
            if ((result = remove_cp_element(i)) != STARJ_OK) {
                sjaf_warning("Failed to remove bad class path entry");
                goto next_iteration;
            }
            continue;
        }

next_iteration:
        i++;
    }
    

    SJAM_END_CLASS_RESOLVER_ACCESS();
    return STARJ_ERR_NOT_FOUND;
}
