package de.hbrs.se2.control.apply;

import de.hbrs.se2.model.apply.ApplicationState;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.apply.ApplyRepository;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.student.Student;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {

    @Autowired
    private ApplyRepository applyRepository;

    public Apply insert(Apply apply) {
        return applyRepository.save(apply);
    }

//    public Apply findApplyByStudent(Student student) {
//
//       // return applyRepository.getApplyByStudent(student.getId());
//    }

    public Apply findApplyByCompany(Company company) {
        throw new NotImplementedException();
    }

    public List<Apply> findAll() {
        return this.applyRepository.findAll();
    }

    public void delete(Apply apply) {
        this.applyRepository.delete(apply);
    }

    public Apply getApplyByStudentAndAdvertisement(Student student, Advertisement advertisement) {
//        Optional<Apply> applyOptional = applyRepository.getApplyByStudentAndAdvertisement(student.getId(), advertisement.getId());
//        if (applyOptional.isPresent()) {
//            return applyOptional.get();
//        } else {
//            // Throw an exception indicating that no company is found by the given email
//            throw new NoSuchElementException("This student " + student.getFirst_name() + "has not applied for this advertisement " + advertisement.getName());
//        }
        return applyRepository.getApplyByStudentAndAdvertisement(student.getId(), advertisement.getId());
    }


    public void acceptApplicant(Apply apply) {
        apply.setState(ApplicationState.ACCEPTED);
        this.insert(apply);
    }

    public void rejectApplicant(Apply apply) {
        apply.setState(ApplicationState.REJECTED);
        this.insert(apply);
    }
}
