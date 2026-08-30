#!/bin/sh
# Pin LaTeX-Workshop to 10.7.1 in the devcontainer.
# Newer versions (>= 10.7.2) break the internal PDF viewer.
EXT='/root/.vscode-server/extensions'

# Remove any LaTeX-Workshop version other than 10.7.1.
ls -d "$EXT"/james-yu.latex-workshop-*/ 2>/dev/null | grep -v 'james-yu.latex-workshop-10.7.1' | xargs -r rm -rf

# Reset obsolete markers so VS Code does not refuse to load 10.7.1.
if [ -f "$EXT/.obsolete" ]; then
    python3 -c 'import json, sys; p=sys.argv[1]; d=json.load(open(p)); d={k:v for k,v in d.items() if not k.startswith("james-yu.latex-workshop")}; json.dump(d, open(p, "w"))' "$EXT/.obsolete"
fi

exit 0