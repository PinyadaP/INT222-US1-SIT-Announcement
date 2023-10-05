package int221.SASBE.controller;

import int221.SASBE.entities.Category;
import int221.SASBE.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"http://localhost", "http://intproj22.sit.kmutt.ac.th"})
//@CrossOrigin(origins = "http://localhost")
//@CrossOrigin(origins = "http://ip22us1.sit.kmutt.ac.th")
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
@Autowired
private CategoryService categoryService;

    @GetMapping("")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Integer categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
