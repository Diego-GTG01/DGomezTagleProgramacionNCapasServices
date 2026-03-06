package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Municipio;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class MunicipioJPADAOImplementation implements IMunicipioJPA {
    @Autowired
    private EntityManager entityManager;


    @Override
    public Result getMunicipioByEstado(int IdEstado) {
        Result Result = new Result();
        try {
            TypedQuery<Municipio> query = entityManager
                    .createQuery("FROM Municipio m WHERE m.Estado.IdEstado = :idEstado ORDER BY m.Nombre ASC", Municipio.class)
                    .setParameter("idEstado", IdEstado);

            List<Municipio> municipiosJPA = query.getResultList();

            Result.Objects = new ArrayList<>(municipiosJPA);
            Result.Correct = true;
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

}
