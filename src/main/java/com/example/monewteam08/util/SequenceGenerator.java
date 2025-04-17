package com.example.monewteam08.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

// 자동 증가 id 생성
@Component
public class SequenceGenerator {

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * Atomically increments and retrieves the next sequence number for a given MongoDB collection.
   *
   * @param collectionName the name of the collection for which to generate the next sequence value
   * @return the incremented sequence number associated with the specified collection
   */
  public long getNextSequence(String collectionName) {
    Query query = Query.query(Criteria.where("_id").is(collectionName));
    Update update = new Update().inc("seq", 1);
    FindAndModifyOptions options = FindAndModifyOptions.options()
        .returnNew(true).upsert(true);

    Sequence seq = mongoTemplate.findAndModify(query, update, options, Sequence.class);
    return seq.getSeq();
  }

  @Data
  public static class Sequence {
    private String id;
    private long seq;
  }
}