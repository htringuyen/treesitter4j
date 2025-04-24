import os
import re
import argparse

def patch_symbol_lookup(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Replace SYMBOL_LOOKUP with custom implementation
    pattern = re.compile(r'static final SymbolLookup SYMBOL_LOOKUP =.*?;', re.DOTALL)
    replacement = (
        '    \n'  #
        '    // Patch: Replace SYMBOL_LOOKUP with a custom lookup\n'
        '    static final SymbolLookup SYMBOL_LOOKUP = ChainedLibraryLookup.INSTANCE.get(LIBRARY_ARENA);\n'
    )
    content, subs = re.subn(pattern, replacement, content)

    if subs == 0:
        print(f"[ERROR] SYMBOL_LOOKUP not found in {file_path}")
    else:
        print(f"Patch: Replace SYMBOL_LOOKUP with a custom lookup in {file_path}")

    # Add static block before final closing brace
    static_block = (
        '    \n'
        '    // Patch: Configure custom memory allocator\n'
        '    static {\n'
        '        ts_set_allocator(malloc$address(), calloc$address(), realloc$address(), free$address());\n'
        '    }\n'
    )

    class_close_pattern = re.compile(r'\n\}', re.MULTILINE)

    match = class_close_pattern.search(content)
    if match:
        insert_pos = match.start()
        content = content[:insert_pos] + '\n' + static_block + content[insert_pos:]
        print(f"Patch: Static block inserted in {file_path}")
    else:
        print(f"[ERROR] Couldn't find class closing brace in {file_path}")

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)



def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file-path", required=True, help="The path of TreeSitter.java file")
    args = parser.parse_args()

    file_path = os.path.abspath(args.file_path)

    patch_symbol_lookup(file_path)

if __name__ == "__main__":
    main()
