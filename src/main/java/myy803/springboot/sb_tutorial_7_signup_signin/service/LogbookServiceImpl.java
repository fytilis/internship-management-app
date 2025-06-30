package myy803.springboot.sb_tutorial_7_signup_signin.service;

import myy803.springboot.sb_tutorial_7_signup_signin.dao.LogbookDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogbookServiceImpl implements LogbookService {

	private final LogbookDAO logbookRepository;

	public LogbookServiceImpl(LogbookDAO logbookRepository) {
		this.logbookRepository = logbookRepository;
	}

	@Override
	public void save(Logbook logbook) {
		logbookRepository.save(logbook);
	}

	@Override
	public List<Logbook> getLogbooksForStudent(Student student) {
		return logbookRepository.findByStudent(student);
	}

	@Override
	public Logbook getById(Long id) {
		return logbookRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		logbookRepository.deleteById(id);
	}
	@Override
	@Transactional
	public void saveEntry(Logbook entry,Student student) {
		entry.setStudent(student);
		entry.setDate(LocalDateTime.now());
		logbookRepository.save(entry);
		
	}
	
	   @Override
	   @Transactional
	    public List<Logbook> getEntriesByStudent(Student student) {
	        return logbookRepository.findByStudent(student);
	    }
	   @Override
	   @Transactional
	   public List<Logbook> findByStudent(Student student) {
	       return logbookRepository.findByStudent(student);
	   }
}

