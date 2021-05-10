package com.mattgray.socialnetworkkata.port;

import java.time.LocalDateTime;

public interface UserController {

    void process(String command, LocalDateTime time);
}
