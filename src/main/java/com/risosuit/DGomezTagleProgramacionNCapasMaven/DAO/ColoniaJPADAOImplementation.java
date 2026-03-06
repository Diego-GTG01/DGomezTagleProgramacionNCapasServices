package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Colonia;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ColoniaJPADAOImplementation implements IColoniaJPA {
    @Autowired
    private EntityManager entityManager;



    @Override
    public Result getColoniaByMunicipio(int IdMunicipio) {
        Result Result = new Result();
        try {
            TypedQuery<Colonia> query = entityManager
                    .createQuery("FROM Colonia c WHERE c.Municipio.IdMunicipio = :idMunicipio ORDER BY c.Nombre ASC",
                            Colonia.class)
                    .setParameter("idMunicipio", IdMunicipio);

            List<Colonia> coloniaJPA = query.getResultList();
            Result.Objects = new ArrayList<>(coloniaJPA);
            Result.Correct = true;
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

}
