import java.util.PriorityQueue;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/14  12:20
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class A {

	public static void main(String[] args) {
//		String s="<font>ABCD<br>AECD<br>AFCQ<br>AWCP</font>";
//		Pattern p=Pattern.compile("(A.*?C.+?)");
//		Matcher m=p.matcher(s);
//		while(m.find()){
//			System.out.println(m.group(1));
//		}

		PriorityQueue<AA> queue = new PriorityQueue<>();

		queue.offer(new AA(1));
		queue.offer(new AA(2));

		System.out.println(queue.poll().getId());
		System.out.println(queue.poll().getId());
	}

	public static class AA implements Comparable<AA>{

		private long id;

		public AA(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		@Override
		public int compareTo(AA o) {
			return id > o.getId() ? 1 : -1;
		}
	}
}
