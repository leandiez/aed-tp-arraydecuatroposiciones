FROM texlive/texlive

# Copiar MACROS y referenciarlas como packages
COPY "texmf" "/root/texmf"
RUN texhash

RUN apt-get update && apt-get install -y --no-install-recommends \
    default-jre default-jdk maven   \
    && rm -rf /var/lib/apt/lists/*