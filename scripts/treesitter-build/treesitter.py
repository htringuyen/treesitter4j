#! use python 3
import argparse
import os
import subprocess
import sys
import tempfile
import shutil

def run_cmake(source_dir, output_dir):
    with tempfile.TemporaryDirectory() as temp_dir:
        build_dir = os.path.abspath(temp_dir)

        #build_dir = os.path.join(source_dir, "build-lib")

        # choose gcc-based generator
        if os.name == 'nt':
            generator = "MinGW Makefiles"
            lib_name = "libtree-sitter.dll"
        elif os.name == 'posix':
            generator = "Unix Makefiles"
            lib_name = "tree-sitter.so"
        else:
            raise RuntimeError("Unsupported OS " + os.name)

        # configure
        subprocess.check_call(["cmake", source_dir, "-G", generator, "-DCMAKE_C_COMPILER=gcc"], cwd=build_dir)

        # build
        subprocess.check_call(["cmake", "--build", "."], cwd=build_dir)

        lib_path = os.path.join(build_dir, lib_name)
        output_path = os.path.join(output_dir, lib_name)

        shutil.copy(lib_path, output_path)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--source-dir", required=True, help="The path of tree-sitter lib source")
    parser.add_argument("--output-dir", required=True, help="Where to place output library")
    args = parser.parse_args()

    source_dir = os.path.abspath(args.source_dir)
    output_dir = os.path.abspath(args.output_dir)

    run_cmake(source_dir, output_dir)

if __name__ == "__main__":
    main()




