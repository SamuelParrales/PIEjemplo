package net.sepp.APPEjemplo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.sepp.APPEjemplo.entities.Receta;


public interface RecetaRepo extends CrudRepository <Receta,Long>{
	List<Receta> findByNombre(String nombre);
	List<Receta> findByPreparacion(String nombre);
}
