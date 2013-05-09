CC=gcc
SRC_DIR=src
LIB_DIR=lib
TEST_DIR=test
AGENT_DIR=${SRC_DIR}/agent
CFREADER_DIR=${SRC_DIR}/cfreader
DATA_DIR=${SRC_DIR}/data

CFLAGS=-Wall -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -O2 -finline-functions -Winline
LIBS=-lz
TARGET=sj
TARGET_LIB=lib${TARGET}.so

AGENT_SOURCES=${AGENT_DIR}/*.c
CFREADER_SOURCES=${CFREADER_DIR}/*.c
DATA_SOURCES=${DATA_DIR}/*.c

AGENT_OBJ=${AGENT_DIR}/agent.o
CFREADER_OBJ=${CFREADER_DIR}/cfreader.o
DATA_OBJ=${DATA_DIR}/data.o

INSTALL_DIR=${HOME}/ldlib/

all: ${LIB_DIR}/${TARGET_LIB}

compile:
	${shell if [ ! -d ${LIB_DIR} ]; then mkdir -p ${LIB_DIR}; fi}
	${CC} ${CFLAGS} -shared ${SOURCES} -o ${LIB_DIR}/${TARGET_LIB} ${LIBS}

${LIB_DIR}/${TARGET_LIB}: ${AGENT_SOURCES} ${DATA_SOURCES} ${CFREADER_SOURCES}
	${shell if [ ! -d ${LIB_DIR} ]; then mkdir -p ${LIB_DIR}; fi}
	${CC} ${CFLAGS} -shared ${AGENT_SOURCES} ${DATA_SOURCES} ${CFREADER_SOURCES} -o ${LIB_DIR}/${TARGET_LIB} ${LIBS}

install: compile
	${shell if [ ! -d ${INSTALL_DIR} ]; then mkdir -p ${INSTALL_DIR}; fi}
	mv ${LIB_DIR}/${TARGET_LIB} ${INSTALL_DIR}

clean:
	rm -fr ${LIB_DIR} ${AGENT_OBJ} ${DATA_OBJ} ${CFREADER_OBJ} *.dat
