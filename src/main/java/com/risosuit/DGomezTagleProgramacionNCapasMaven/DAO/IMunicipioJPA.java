package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

public interface IMunicipioJPA {
    public Result getMunicipioByEstado(int IdEstado);

}
