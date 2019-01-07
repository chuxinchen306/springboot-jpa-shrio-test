package hello.shrio;



import hello.admin.AdminDo;
import hello.admin.MenuRepository;
import hello.admin.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MenuRepository menuRepository;


	@Override
	public Class getAuthenticationTokenClass() {
		return UsernamePasswordToken.class;
	}

	@Override
	public String getName() {
		return "UserRealm";
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		AdminDo adminDo = (AdminDo)SecurityUtils.getSubject().getPrincipal();

		//TODO 菜单鉴权
		Set<String> perms = menuRepository.listPerms(adminDo.getUserId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();

		String password = new String((char[]) token.getCredentials());

		// 查询用户信息
		AdminDo user = userRepository.findByUserName(username);

		if (user == null) {
			// 账号不存在
			throw new AuthenticationException("010020");
		}

//		if (!PasswordUtils.compare(password,user.getPassword(),user.getSalt())) {
//			// 密码错误
//			throw new AuthenticationException("010020");
//		}

//		SessionUser sessionUser = new SessionUser();
//		BeanUtils.copyProperties(user,sessionUser);

		
		// TODO 账号锁定
		/*if (user.getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}*/
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

//	/**
//	 * 清除所有用户的缓存
//	 */
//	public void clearAuthorizationInfoCache() {
//		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
//		if(cache!=null) {
//			cache.clear();
//		}
//	}
//
//	/**
//	 * 清除指定用户的缓存
//	 * @param user
//	 */
//	private void clearAuthorizationInfoCache(SessionUser user) {
//		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
//		cache.remove(user.getUserId());
//	}

}
