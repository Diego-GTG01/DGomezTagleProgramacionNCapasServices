package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

public interface IColoniaJPA {
    public Result getColoniaByMunicipio(int IdMunicipio);
}
