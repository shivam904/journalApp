package net.shivamcoder.journalApp.Scheduler;

import net.shivamcoder.journalApp.Enums.Sentiment;
import net.shivamcoder.journalApp.entity.JournalEntry;
import net.shivamcoder.journalApp.entity.User;
import net.shivamcoder.journalApp.repository.UserRepoImpl;
import net.shivamcoder.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    private EmailService emailService;
    private UserRepoImpl userRepoImpl;
    @Autowired
    public UserScheduler(EmailService emailService, UserRepoImpl userRepoImpl) {
        this.emailService = emailService;
        this.userRepoImpl = userRepoImpl;
    }
    @Scheduled(cron="0 0 9 * * SUN")
    public void fetchUsersAndSendMail(){
        List<User> users=userRepoImpl.getUserForSentimentAnalysis();
        for (User user :users){
            List<JournalEntry>journalEntries=user.getJournalEntries();
            List<Sentiment>filterdEntries= journalEntries.stream().filter(x->x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer>sentimentCount=new HashMap<>();
            for(Sentiment sentiment: filterdEntries){
                if(sentiment !=null){
                    sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment,0)+1);

                }
            }
            Sentiment mostFrequentSentiment=null;
            int maxCount=0;
            for (Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount=entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }

            }
            if(mostFrequentSentiment !=null){
                emailService.sendMail(user.getEmail(),"Sentiment analysis for last 7 days : ",mostFrequentSentiment.toString());
            }



        }
    }
}
