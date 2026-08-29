FROM danteev/texlive

# Copiar MACROS y referenciarlas como packages
COPY "texmf" "/root/texmf"
RUN texhash

# Install tools
RUN pip install arxiv-latex-cleaner==0.1.* pdflinkchecker-cli==0.2.* --break-system-packages