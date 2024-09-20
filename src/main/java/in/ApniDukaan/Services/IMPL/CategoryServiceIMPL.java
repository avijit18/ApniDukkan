package in.ApniDukaan.Services.IMPL;

import in.ApniDukaan.Entities.Category;
import in.ApniDukaan.Repositories.CategoryRepository;
import in.ApniDukaan.Services.Interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceIMPL implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(category)) {
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return categoryRepository.findAll(pageable);
    }
}
