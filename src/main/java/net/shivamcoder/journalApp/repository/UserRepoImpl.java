package net.shivamcoder.journalApp.repository;

import net.shivamcoder.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepoImpl {

    private MongoTemplate mongotemplate;
    @Autowired
    public UserRepoImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    public List<User>getUserForSentimentAnalysis(){
        Query query= new Query();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));

        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users=mongotemplate.find(query,User.class);
        return users;

    }
}
