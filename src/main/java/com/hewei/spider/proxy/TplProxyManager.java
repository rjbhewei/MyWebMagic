//package com.hewei.spider.proxy;
//
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.NavigableSet;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//import java.util.function.Consumer;
//import java.util.stream.IntStream;
//import java.util.stream.IntStream.Builder;
//
//import org.apache.http.HttpHost;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//
///**
// * 代理管理器
// *
// * @author zhangnan1
// *
// */
//public class TplProxyManager {
//	// private Logger logger = LoggerFactory.getLogger(TplProxyManager.class);
//
//	private TplProxyManager() {
//	}
//
//	private static TplProxyManager ent = new TplProxyManager();
//
//	public static TplProxyManager instance() {
//		return ent;
//	}
//
//	private static java.util.function.Predicate<Integer> portPredicate;
//
//	/**
//	 * 借出代理
//	 *
//	 * @return
//	 */
//	public HttpHost loan() {
//		for (;;) {
//			TplProxy tplProxy;
//			if (set.size() < MIN_PROXY_NUM) {
//				loadProxy(100);
//			}
//			try {
//				lock.readLock().lock();
//				tplProxy = set.pollFirst();
//			} finally {
//				lock.readLock().unlock();
//			}
//			if (null == tplProxy) {
//				loadProxy(100);
//				continue;
//			}
//			System.out.println("loan:" + tplProxy.score + ":"
//					+ tplProxy.getHttpHost());
//			return tplProxy.getHttpHost();
//		}
//	}
//
//	private boolean report0(TplProxy tplProxy, int weight) {
//		weighting(tplProxy, weight);
//		if (tplProxy.getScore() >= 0) {
//			System.out.println("[还可用0]:" + tplProxy.score + ":"
//					+ tplProxy.getHttpHost());
//			// 还能用返回true
//			return true;
//		} else {
//			System.out.println("[抛弃0]:" + tplProxy.score + ":"
//					+ tplProxy.getHttpHost());
//			// 不能用了，删除并返回false
//			try {
//				lock.writeLock().lock();
//				map.remove(tplProxy.getHttpHost());
//			} finally {
//				lock.writeLock().unlock();
//			}
//			return false;
//		}
//
//	}
//
//	/**
//	 * 报告代理使用状态
//	 *
//	 * @param httpHost
//	 * @param weight
//	 * @return
//	 */
//	public boolean report(HttpHost httpHost, int weight) {
//		AtomicBoolean res = new AtomicBoolean();
//		edit0(httpHost, (tplProxy) -> {
//			res.set(report0(tplProxy, weight));
//		});
//		return res.get();
//	}
//
//	/**
//	 * 报告代理使用状态
//	 *
//	 * @param httpHost
//	 * @param info
//	 * @return 是否可以继续使用 true为可以继续使用,返回false不可继续使用
//	 */
//	public boolean report(HttpHost httpHost, ProxyInfo info) {
//		AtomicBoolean res = new AtomicBoolean(false);
//		edit0(httpHost, (tplProxy) -> {
//			res.set(report0(tplProxy, calculation(tplProxy, info)));
//		});
//		return res.get();
//	}
//
//	/**
//	 * 报告代理使用状态
//	 *
//	 * @param httpHost
//	 * @param infoString
//	 * @return
//	 */
//	public boolean report(HttpHost httpHost, String infoString) {
//		return report(httpHost, ProxyInfo.parse(infoString));
//	}
//
//	private void revert0(TplProxy tplProxy, int weight) {
//		System.out.println("[归还代理]:" + tplProxy.getHttpHost());
//		weighting(tplProxy, weight);
//		if (tplProxy.getScore() >= 0) {
//			System.out.println("[还可用1]:" + tplProxy.score + ":"
//					+ tplProxy.getHttpHost());
//			try {
//				lock.writeLock().lock();
//				set.add(tplProxy);
//			} finally {
//				lock.writeLock().unlock();
//			}
//		} else {
//			try {
//				lock.writeLock().lock();
//				map.remove(tplProxy.getHttpHost());
//			} finally {
//				lock.writeLock().unlock();
//			}
//			System.out.println("[抛弃1]:" + tplProxy.score + ":"
//					+ tplProxy.getHttpHost());
//
//		}
//	}
//
//	/**
//	 * 归还代理
//	 *
//	 * @param httpHost
//	 * @param weight
//	 */
//	public void revert(HttpHost httpHost, int weight) {
//		edit0(httpHost, (tplProxy) -> {
//			revert0(tplProxy, weight);
//		});
//	}
//
//	/**
//	 * 归还代理
//	 *
//	 * @param httpHost
//	 * @param info
//	 */
//	public void revert(HttpHost httpHost, ProxyInfo info) {
//		edit0(httpHost, (tplProxy) -> {
//			revert0(tplProxy, calculation(tplProxy, info));
//		});
//	}
//
//	private void edit0(HttpHost httpHost, Consumer<TplProxy> action,
//			Consumer<HttpHost> errorAction) {
//		if (null == httpHost) {
//			System.out.println("[传入的httpHost为空]");
//		}
//		TplProxy tpl = null;
//		try {
//			lock.readLock().lock();
//			tpl = map.get(httpHost);
//		} finally {
//			lock.readLock().unlock();
//		}
//		if (null != tpl) {
//			action.accept(tpl);
//		} else {
//			errorAction.accept(httpHost);
//		}
//	}
//
//	private void edit0(HttpHost httpHost, Consumer<TplProxy> action) {
//		edit0(httpHost, action,
//				(host) -> System.out.println("[不是代理池中的代理]" + host));
//	}
//
//	/**
//	 * 归还代理
//	 *
//	 * @param httpHost
//	 * @param infoString
//	 */
//	public void revert(HttpHost httpHost, String infoString) {
//		revert(httpHost, ProxyInfo.parse(infoString));
//	}
//
//	/**
//	 * 读写锁。
//	 */
//	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//
//	private NavigableSet<TplProxy> set = Sets
//			.newTreeSet((a, b) -> {
//				if (a.score > b.score) {
//					return -1;
//				} else if (a.score < b.score) {
//					return 1;
//				}
//				return a.getHttpHost().toString()
//						.compareTo(b.getHttpHost().toString());
//			});
//	private LinkedHashMap<HttpHost, TplProxy> map = new LinkedHashMap<HttpHost, TplProxy>() {
//		private static final long serialVersionUID = 5183638451881541422L;
//		/**
//		 * 如果数量大于1500 ，上次使用时间超过两分钟的删除。
//		 */
//		protected boolean removeEldestEntry(
//				java.util.Map.Entry<HttpHost, TplProxy> eldest) {
//			if (this.size() > 1500
//					&& Math.abs(System.currentTimeMillis()
//							- eldest.getValue().getLastTime()) > 120000) {
//				set.remove(eldest.getValue());
//				return true;
//			}
//			return false;
//		};
//	};
//
//	private Iterator<HttpHost> iterator;
//	private static final int MIN_PROXY_NUM = 150;
//
//	/**
//	 * 添加代理
//	 *
//	 * @param httpHost
//	 */
//	void addProxy(HttpHost httpHost) {
//		TplProxy tplProxy = new TplProxy(httpHost);
//		try {
//			lock.writeLock().lock();
//			set.add(tplProxy);
//			map.put(tplProxy.getHttpHost(), tplProxy);
//		} finally {
//			lock.writeLock().unlock();
//		}
//	}
//
//	public void setSource(Iterable<HttpHost> itr) {
//		try {
//			lock.writeLock().lock();
//			iterator = itr.iterator();
//		} finally {
//			lock.writeLock().unlock();
//		}
//	}
//
//	void loadProxy(int num) {
//		List<HttpHost> tmp = Lists.newArrayList();
//		if (null == iterator) {
//			setSource(ProxySource.allProxySource());
//		}
//		try {
//			lock.writeLock().lock();
//			for (int i = 0; i < num;) {
//				if (iterator.hasNext()) {
//					HttpHost host = iterator.next();
//					if (null != portPredicate
//							&& !portPredicate.test(host.getPort())) {
//						continue;
//					}
//					addProxy(host);
//					tmp.add(host);
//					i++;
//				}
//			}
//		} finally {
//			lock.writeLock().unlock();
//		}
//		System.out.println("loaded:" + tmp);
//	}
//
//	final static private int MAX_TIME = 50000;
//	final static private int BASE_NUMBER = 20000;
//
//	/**
//	 * 评估代理
//	 *
//	 * @param proxy
//	 * @param info
//	 */
//	void reckon(TplProxy proxy, ProxyInfo info) {
//		weighting(proxy, calculation(proxy, info));
//	}
//
//	/**
//	 * 计算权重
//	 *
//	 * @param proxy
//	 *            代理
//	 * @param info
//	 *            代理使用信息
//	 * @return 权重加成基准值
//	 */
//	int calculation(TplProxy proxy, ProxyInfo info) {
//		Builder scoreStream = IntStream.builder();
//		// 计算间隔时间分数，间隔越低分数越高。最高300分
//		int spke = (BASE_NUMBER / ((int) (System.currentTimeMillis() - proxy
//				.getLastTime()) + 1));
//		scoreStream.add(spke > 300 ? 300 : spke);
//		// 计算连接时间与读取时间
//		int fx = MAX_TIME / (info.getConnectTime() + info.getReadTime() + 1);
//		scoreStream.add(fx > 500 ? 500 : fx);
//		// 计算状态
//		if (info.getCodeStatus() == 200) {
//			scoreStream.add(500);
//		} else if (info.getCodeStatus() >= 400 && info.getCodeStatus() <= 500) {
//			scoreStream.add(-200);
//		} else if (info.getCodeStatus() >= 600) {
//			scoreStream.add(-300);
//		} else {
//			scoreStream.add(-500);
//		}
//		return scoreStream.build().reduce((a, b) -> a + b).getAsInt();
//	}
//
//	/**
//	 * 加权
//	 *
//	 * @param proxy
//	 * @param weight
//	 */
//	void weighting(TplProxy proxy, int weight) {
//		// 计算当前分数
//		int thisScore = (weight / 5) + ((proxy.getScore() * 4) / 5);
//		proxy.setLastTime(System.currentTimeMillis());
//		proxy.setScore(thisScore);
//	}
//
//	public int allSize() {
//		return map.size();
//	}
//
//	public int availableSize() {
//		return set.size();
//	}
//
//	public String info() {
//		return "[总数:" + allSize() + ",可用:" + availableSize() + "]";
//	}
//
//	public static void setPortPredicate(
//			java.util.function.Predicate<Integer> portPredicate) {
//		TplProxyManager.portPredicate = portPredicate;
//	}
//}
