package direct.interceptor.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * This agent builder listener emptily implements all method in AgentBuilder.Listener
 *     - making it easier to selectively override any of the method.
 * 
 * @author NawaMan
 */
class $AgentListener implements AgentBuilder.Listener {
	
	@Override
    public void onComplete(String arg0) { }
	
    @Override
    public void onError(String errMsg, Throwable throwable) { }
    
    @Override
    public void onTransformation(TypeDescription arg0, DynamicType arg1) { }
    
    @Override
    public void onIgnored(TypeDescription typeDescription) { }
    
};