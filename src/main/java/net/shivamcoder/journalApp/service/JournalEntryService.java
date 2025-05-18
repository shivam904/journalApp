package net.shivamcoder.journalApp.service;

import net.shivamcoder.journalApp.entity.JournalEntry;
import net.shivamcoder.journalApp.entity.User;
import net.shivamcoder.journalApp.repository.JournalRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    private final JournalRepo journalRepo;
    private final UserService userService;
    @Autowired
    public JournalEntryService(JournalRepo journalRepo, UserService userService) {
        this.journalRepo = journalRepo;
        this.userService = userService;
    }


    private static final Logger logger= LoggerFactory.getLogger(JournalEntryService.class);
    @Transactional
    public void saveEntry(JournalEntry journalEntry , String userName){
        try{
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);

        }catch (Exception e){

            System.out.println(e);
            throw new RuntimeException("SAVE ENTRY mai code fat gya hai",e);
        }


}

    public void saveEntry2(JournalEntry journalEntry ){

        JournalEntry saved= journalRepo.save(journalEntry);


    }
    public List<JournalEntry>getAll(){
        return journalRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalRepo.findById(id);
    }
    @Transactional
    public void deleteById(ObjectId id,String userName){
        try {
            User user= userService.findByUserName(userName);
            boolean removed=user.getJournalEntries().removeIf(x->x.getId().equals(id));
            if(removed) {
                userService.saveEntry(user);
                journalRepo.deleteById(id);
            }

        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("Unable to delete",e);

        }

    }


}
