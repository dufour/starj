#!/bin/bash

gcc -DCFREADER_DEBUG_MODE -I.. main.c ../*.c -o cfread -lz
