import argparse
import os
import subprocess
import tempfile
import shutil
import common

def build_parsers(grammars_dir, output_dir):
    with open(os.path.join(grammars_dir, "included_grammars.txt"), "r") as file:
        grammar_names = [line.strip() for line in file if line.strip()]

    for grammar_name in grammar_names:
        source_dir = os.path.join(grammars_dir, grammar_name)

        os_libname_map = {
            "nt": "lib" + grammar_name + ".dll",
            "posix": grammar_name + ".so"
        }

        os_generator_map = {
            "nt": "MinGW Makefiles",
            "posix": "Unix Makefiles"
        }

        common.build_with_cmake(source_dir, output_dir, os_libname_map, os_generator_map, "lib" + grammar_name)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--grammars-dir", required=True, help="Parent directory of grammar projects")
    parser.add_argument("--output-dir", required=True, help="Output directory of built libraries")
    args = parser.parse_args()

    grammar_dir = os.path.abspath(args.grammars_dir)
    output_dir = os.path.abspath(args.output_dir)

    build_parsers(grammar_dir, output_dir)

if __name__ == "__main__":
    main()



