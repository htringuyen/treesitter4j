#! use python 3
import argparse
import os
import subprocess
import sys
import tempfile
import shutil
import common

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--source-dir", required=True, help="The path of tree-sitter lib source")
    parser.add_argument("--output-dir", required=True, help="Where to place output library")
    args = parser.parse_args()

    source_dir = os.path.abspath(args.source_dir)
    output_dir = os.path.abspath(args.output_dir)

    os_libname_map = {
        "nt": "libtree-sitter.dll",
        "posix": "tree-sitter.so"
    }

    os_generator_map = {
        "nt": "MinGW Makefiles",
        "posix": "Unix Makefiles"
    }

    common.build_with_cmake(source_dir, output_dir, os_libname_map,
                            os_generator_map, sanitized_libname="libtree-sitter")

if __name__ == "__main__":
    main()




