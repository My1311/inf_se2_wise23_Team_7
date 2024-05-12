package de.hbrs.se2.control.rating;

import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.rating.Rating;
import de.hbrs.se2.model.rating.RatingRepository;
import de.hbrs.se2.model.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }


    public Rating findRatingByStudentAndCompany(Student student, Company company) {
        return ratingRepository.findRatingByStudentAndCompany(student, company);
    }

    public void delete(Rating rating) {
        ratingRepository.delete(rating);
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public List<Rating> findAllOfRatingInCompany(Company company) {
        return ratingRepository.findRatingByCompany(company.getId());
    }

    public double getAverageRatingOfCompany(Company company) {
        List<Rating> ratings = ratingRepository.findRatingByCompany(company.getId());
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getValue();
        }
        return sum / ratings.size();
    }
}
