package model;

import exception.SpringRedittException;
import org.springframework.mail.MailException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) {
        MailException e = null;
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringRedittException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
