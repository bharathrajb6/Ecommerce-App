package com.example.user_service.Repository;

import com.example.user_service.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    /***
     * This method is used to find all tokens based on username and isLoggedOut
     * @param username
     * @return
     */
    @Query("""
            select t from Token t inner join User u on t.user.username=u.username where t.user.username=:username and t.isLoggedOut=false
            """)
    List<Token> findAllTokens(String username);

    /***
     * This method is used to find token based on tokenID
     * @param token
     * @return
     */
    Optional<Token> findByToken(String token);
}
