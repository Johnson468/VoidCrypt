import java.io.File;

public class VoidFile extends File{
	private boolean isVCEncrypted;
	private String path;
	public VoidFile(String path) {
		super(path);
		this.path = path;
		isVCEncrypted = getExtension(path).contains("VC");
		// TODO Auto-generated constructor stub
	}
	public boolean getIsVCEncrypted() {
		return isVCEncrypted;
	}
	public void setIsVCEncrypted() {
		isVCEncrypted = getExtension(path).substring(0,1).equals("VC");
	}
	public String getExtension(String path) {
		if ( path.lastIndexOf('.') > 0) {
		    return path.substring(path.lastIndexOf('.')+1);
		}
		return null;
	}
	public void setExtension(String path) {
		VoidFile vf = new VoidFile(path.replace("\\", "/"));
			vf.renameTo(new File(path + "VC" + getExtension(path)));
	}
}
