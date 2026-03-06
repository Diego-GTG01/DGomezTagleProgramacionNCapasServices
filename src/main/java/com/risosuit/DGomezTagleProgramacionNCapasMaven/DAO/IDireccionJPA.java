package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Direccion;

public interface IDireccionJPA {
    public Result GetById(int IdDireccion);

    public Result Add(Direccion Direccion, int idUsuario);
    public Result Update(Direccion Direccion, int idUsuario);
    public Result Delete(int idDireccion);
;

}
