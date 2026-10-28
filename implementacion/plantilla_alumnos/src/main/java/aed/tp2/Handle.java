package aed.tp2;

interface Handle<T>{    
    /**
     * Devuelve el valor del elemento
     * 
     */
    public T valor();
    
    /**
     * Elimina al elemento de la colección
     * 
     */
    public void eliminar();

    /**
     * Modifica al elemento de la colección y su orden (de ser necesario)
     * 
     */
    public void modificar(T v);

    /**
     * Devuelve si el valor que representa el Handle es equivalente al que representa otro.
     * 
     */
    public boolean equals(Handle<T> h);

    

}
