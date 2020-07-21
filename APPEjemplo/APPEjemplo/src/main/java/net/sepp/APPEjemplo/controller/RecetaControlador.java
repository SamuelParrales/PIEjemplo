package net.sepp.APPEjemplo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.sepp.APPEjemplo.entities.Receta;
import net.sepp.APPEjemplo.repository.RecetaRepo;
import net.sepp.APPEjemplo.service.PictureService;



@Controller
@RequestMapping("/recetas")
public class RecetaControlador {
	@Autowired
	private RecetaRepo repo;
	
	@Autowired
	PictureService picService;
	
	@RequestMapping("")
	public String index() {
		return "index";
	}
	
	@GetMapping("/add")
	public String showSingUpForm(Receta receta) {
		return "add_recipe";
	}
	
	@GetMapping("/list")
	public String showRecipes(Model model) {
		model.addAttribute("recipes", repo.findAll());
		return "list_recipes";
	}
	
	@RequestMapping("/login")
	public String ShowLogin() {
		return "login";
	}
	
	@GetMapping("/sc")
	public String showRecipes() {
		return "soundcloud.html";
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/private")
	public String showPrivate(Model model) {
		model.addAttribute("recipes",repo.findAll() );
		return "list_recipes";
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/add")
	public String AddRecipe(Receta receta, BindingResult result, Model model, @RequestParam("file") MultipartFile file) {
		if (result.hasErrors()) {
			return "add_recipe";
		}
		UUID idPic = UUID.randomUUID();
		picService.uploadPicture(file, idPic);
		receta.setFoto(idPic);
		repo.save(receta);
		return "redirect:list";
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/edit/{id]")
	public String ShowUpdateForm(@PathVariable("id") long id, Model model) {
		Receta receta= repo.findById(id).orElseThrow( () -> new IllegalArgumentException("invalid recipe id: "+id));
		model.addAttribute("receta",receta);
		return "update_recipe";
	}
	
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/update/{id]")
	public String UpdateRecipe(@PathVariable("id") long id, Receta receta, BindingResult result, Model model, @RequestParam("file") MultipartFile file) {
		if (result.hasErrors()) {
			receta.setId(id);
			return "update_recipe";
		}
		if (!file.isEmpty()) {
			picService.deletePicture(receta.getFoto());
			UUID idPic= UUID.randomUUID();
			picService.uploadPicture(file, idPic);
			receta.setFoto(idPic);
		}
		repo.save(receta);
		return "redirect:/recetas/list";
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/delete/{id]")
	public String deleteRecipe(@PathVariable("id") long id, Model model) {
		Receta receta= repo.findById(id).orElseThrow( () -> new IllegalArgumentException("invalid recipe id: "+id));
		picService.deletePicture(receta.getFoto());
		repo.delete(receta);
		model.addAttribute("recipes", repo.findAll());
		return "list_recipes";
	}
	
	
}