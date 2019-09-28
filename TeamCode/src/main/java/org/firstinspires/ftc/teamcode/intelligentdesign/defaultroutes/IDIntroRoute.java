package org.firstinspires.ftc.teamcode.intelligentdesign.defaultroutes;

import fi.iki.elonen.NanoHTTPD;
import org.firstinspires.ftc.teamcode.intelligentdesign.IDHandler;
import org.firstinspires.ftc.teamcode.intelligentdesign.IDResponse;

import java.util.List;
import java.util.Map;

public class IDIntroRoute implements IDHandler {
    private String IDVersion;

    public IDIntroRoute(String IDVersion) {
        this.IDVersion = IDVersion;
    }

    @Override
    public IDResponse onAction(NanoHTTPD.Method method, String route, Map<String, List<String>> parameters) {
        return new IDResponse("Welcome to Intelligent Design version " + IDVersion + ".\n Intelligent Design was created by FTC Team 17126 Natural Selection.");
    }
}
