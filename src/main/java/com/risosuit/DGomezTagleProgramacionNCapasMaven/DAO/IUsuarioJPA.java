package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.List;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Usuario;

public interface IUsuarioJPA {

    Result GetAll();

    Result GetById(int IdUsuario);

    Result Busqueda(Usuario usuario);

    Result Add(Usuario usuario);

    Result Update(Usuario usuario);

    Result Delete(int IdUsuario);

    Result UpdateActivo(int IdUsuario);

    public Result UpdateImagen(Usuario usuario);

    public Result AddAll(List<Usuario> Usuarios);

}
