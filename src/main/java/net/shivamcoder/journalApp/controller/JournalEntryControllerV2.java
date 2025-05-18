package net.shivamcoder.journalApp.controller;

import net.shivamcoder.journalApp.entity.JournalEntry;
import net.shivamcoder.journalApp.entity.User;
import net.shivamcoder.journalApp.service.JournalEntryService;
import net.shivamcoder.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    private final JournalEntryService journalEntryService;
    private final UserService userService;
    @Autowired
    public JournalEntryControllerV2(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?>getAllJournalEntriesOfUser() {
        Authentication authenticate= SecurityContextHolder.getContext().getAuthentication();
        String userName=authenticate.getName();
        User user= userService.findByUserName(userName);
        List<JournalEntry>all=user.getJournalEntries();
        if(all != null){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authenticate= SecurityContextHolder.getContext().getAuthentication();
            String userName=authenticate.getName();

            journalEntryService.saveEntry(myEntry,userName);
            return new  ResponseEntity<>(myEntry,HttpStatus.CREATED);

        }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId) {
        Authentication authenticate= SecurityContextHolder.getContext().getAuthentication();
        String userName=authenticate.getName();
        User user= userService.findByUserName(userName);
        List<JournalEntry>entry= user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!entry.isEmpty()){
            Optional<JournalEntry>journalEntry=journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    @DeleteMapping("id/{myId}")
    public boolean deleteById(@PathVariable ObjectId myId ) {
        Authentication authenticate= SecurityContextHolder.getContext().getAuthentication();
        String userName=authenticate.getName();
        journalEntryService.deleteById(myId,userName);
        return true;
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId myId, @RequestBody JournalEntry myEntry) {
        Authentication authenticate= SecurityContextHolder.getContext().getAuthentication();
        String userName=authenticate.getName();
        User user= userService.findByUserName(userName);
        List<JournalEntry>entry= user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!entry.isEmpty()){
            Optional<JournalEntry>journalEntryOld=journalEntryService.findById(myId);
            if(journalEntryOld.isPresent()){
                JournalEntry old= journalEntryOld.get();
                old.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle(): old.getTitle());
                old.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent():old.getContent());
                journalEntryService.saveEntry2(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
