package com.mattgray.socialnetworkkata.port;

import java.io.IOException;
import java.time.Clock;

public interface UserController {

    void process(Clock clock) throws IOException;
}
