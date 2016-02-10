package uk.sponte.automation.seleniumpom.proxies.handlers;

import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by evops on 05/02/2016.
 */
public class FrameSwitchingHandler implements InvocationHandler {

    private final FrameWrapper frame;
    private final WebDriverFrameSwitchingOrchestrator frameSwitchingOrchestrator;

    public FrameSwitchingHandler(FrameWrapper frame, WebDriverFrameSwitchingOrchestrator frameSwitchingOrchestrator) {
        this.frame = frame;
        this.frameSwitchingOrchestrator = frameSwitchingOrchestrator;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        frameSwitchingOrchestrator.useFrame(frame);
        return method.invoke(o, objects);
    }
}
