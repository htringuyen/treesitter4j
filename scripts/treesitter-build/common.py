import os
import subprocess
import tempfile
import shutil

def build_with_cmake(source_dir, output_dir, os_libname_map, os_generator_map, sanitized_libname=None):
    check_os_support(os_libname_map, os_generator_map)

    with tempfile.TemporaryDirectory() as temp_dir:
        build_dir = os.path.abspath(temp_dir)

        if os.name in os_libname_map and os.name in os_generator_map:
            generator = os_generator_map[os.name]
            lib_name = os_libname_map[os.name]
        else:
            raise RuntimeError("Unsupported OS: " + os.name)

        # configure
        subprocess.check_call(["cmake", source_dir, "-G", generator, "-DCMAKE_C_COMPILER=gcc"], cwd=build_dir)

        # build
        subprocess.check_call(["cmake", "--build", "."], cwd=build_dir)

        lib_path = os.path.join(build_dir, lib_name)

        if sanitized_libname is None:
            output_path = os.path.join(output_dir, lib_name)
        else:
            _, ext = os.path.splitext(lib_name)
            output_path = os.path.join(output_dir, sanitized_libname + ext)

        shutil.copy(lib_path, output_path)

def check_os_support(os_libname_map, os_generator_map):
    if len(os_generator_map) < 1:
        raise RuntimeError("No OS supported")

    if len(os_generator_map) != len(os_libname_map):
        raise RuntimeError("Generators for each OS must match lib names provided")

    for os in os_generator_map.keys():
        if not os in os_libname_map:
            raise RuntimeError("Generators for each OS must match lib names provided")


























