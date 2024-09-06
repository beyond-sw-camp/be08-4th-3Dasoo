package com.fourstit.pocari.repository;

import com.fourstit.pocari.entity.Category;
import com.fourstit.pocari.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
