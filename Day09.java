import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Day09 extends DayRunner {
	private List<String> lines;
	private List<File> files;
	private List<File> empty;

	Day09(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.lines = getLines(fileName);
	}

	private void parseLines() {
		int fileId = 0;
		int sector = 0;

		this.files = new ArrayList<>();
		this.empty = new ArrayList<>();

		for(String line: lines) {
			for(int i = 0; i < line.length(); i++) {
				int len = Integer.valueOf("" + line.charAt(i));

				if (i % 2 == 0) {
					File f = new File(fileId, sector, len);
					this.files.add(f);
					fileId++;
				} else {
					File f = new File(-1, sector, len);
					this.empty.add(f);
				}

				sector += len;
			}
		}
	}

	protected Integer getDayNumber() {
		return 9;
	}

	protected String getExpectedFirstResult() {
		return "1928";
	}

	protected String getExpectedSecondResult() {
		return "2858";
	}

	private boolean defrag_by_sector() {
		File first_empty = empty.remove(0);
		File last_file = files.remove(files.size() - 1);

		if (first_empty.length < last_file.length) {
			File new_file = new File(last_file.fileId, first_empty.sector, first_empty.length);
			File copy_first_empty = new File(-1, last_file.sector + last_file.length - first_empty.length, first_empty.length);
			File copy_last_file = new File(last_file.fileId, last_file.sector, last_file.length - first_empty.length);

			this.files.add(new_file);
			this.files.add(copy_last_file);
			this.empty.add(copy_first_empty);
		} else {
			int sector = last_file.sector;

			File copy_last_file = new File(last_file.fileId, first_empty.sector, last_file.length);
			File copy_first_empty = new File(-1, first_empty.sector + last_file.length, first_empty.length - last_file.length);

			this.files.add(copy_last_file);

			if (copy_first_empty.length > 0) {
				File new_empty = new File(-1, sector, last_file.length);
				this.empty.add(copy_first_empty);
				this.empty.add(new_empty);
			} else {
				copy_first_empty = new File(-1, sector, last_file.length);
				this.empty.add(copy_first_empty);
			}
		}

		Collections.sort(this.files, new SortBySector());
		Collections.sort(this.empty, new SortBySector());

		first_empty = this.empty.get(0);
		last_file = this.files.get(this.files.size() - 1);

		return first_empty.sector < last_file.sector;
	}

	protected String getFirstResult() {
		parseLines();

		while(defrag_by_sector());

		long sum = 0;

		for(File f: this.files) {
			for(int s = f.sector; s < f.sector + f.length; s++) {
				sum += (s * f.fileId);
			}
		}

		return Long.toString(sum);
	}

	private boolean defrag_by_file() {
		for(int i = this.files.size() - 1; i >= 0; i--) {
			File f = this.files.get(i);

			boolean fits = false;

			File e = null;
			int j = 0;

			for(; j < this.empty.size(); j++) {
				e = this.empty.get(j);

				if (f.length <= e.length && f.sector > e.sector) {
					fits = true;
					break;
				}
			}

			if (fits) {
				f = this.files.remove(i);
				e = this.empty.remove(j);

				int sector = f.sector;

				File copy_file = new File(f.fileId, e.sector, f.length);
				this.files.add(copy_file);

				if (e.length - f.length > 0) {
					File copy_empty = new File(-1, e.sector + f.length, e.length - f.length);
					this.empty.add(copy_empty);
				}

				File new_empty = new File(-1, sector, f.length);
				this.empty.add(new_empty);

				Collections.sort(this.files, new SortBySector());
				Collections.sort(this.empty, new SortBySector());

				return true;
			}
		}

		return false;
	}

	protected String getSecondResult() {
		parseLines();

		while(defrag_by_file());

		long sum = 0;

		for(File f: this.files) {
			for(int s = f.sector; s < f.sector + f.length; s++) {
				sum += (s * f.fileId);
			}
		}

		return Long.toString(sum);
	}

	public static void main(String[] args) {
		Boolean debug = false;
		Boolean runTests = true;
		Boolean runActual = true;

		for(String arg: args) {
			if (arg.equals("--skip-tests")) {
				runTests = false;
			} else if (arg.equals("--only-tests"))  {
				runActual = false;
			} else if (arg.equals("--debug")) {
				debug = true;
			}
		}

		new Day09(debug, runTests, runActual).Run();
	}

	public class File {
		public final int fileId;
		public final int sector;
		public final int length;

		public File(int fileId, int sector, int length) {
			this.fileId = fileId;
			this.sector = sector;
			this.length = length;
		}
	}

	public class SortBySector implements Comparator<File> {
		public int compare(File a, File b) {
			return a.sector - b.sector;
		}
	}
}
