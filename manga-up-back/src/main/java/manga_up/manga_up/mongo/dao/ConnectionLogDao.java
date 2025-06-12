package manga_up.manga_up.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import manga_up.manga_up.mongo.model.ConnectionLog;

public interface ConnectionLogDao extends MongoRepository<ConnectionLog, String> {
}
