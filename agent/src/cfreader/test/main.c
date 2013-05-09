#include <stdio.h>
#include "cfreader.h"

#define BUFF_SIZE 4096
static char buff[BUFF_SIZE];

int main(int argc, char *argv[]) {
    cfrt_file *f;
    cfrt_classfile classFile;

    if (argc != 2) {
        fprintf(stderr, "Usage: %s <class_file>\n", argv[0]);
        return 1;
    }
    
    f = cfrf_fopen_regular(argv[1], "rb");
    fprintf(stderr, "CFREADER_OK is %d\n", CFREADER_OK);
    fprintf(stderr, "Result is %d\n", cfrf_parse_class(&classFile, f));
    buff[0] = '\0';
    cfrf_get_class_name(&classFile, buff, BUFF_SIZE);
    fprintf(stderr, "Class name is \"%s\"\n", buff);
    //fprintf(stderr, "Releasing class file\n");
    //cfrf_release_class(&classFile);
    //fprintf(stderr, "Class file released\n");

    fprintf(stderr, "Closing file\n");
    cfrf_fclose(f);
    fprintf(stderr, "File closed\n");

    return 0;
}
