import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<Tree> getAllNodes(String filepath) {
        List<Tree> list = new ArrayList<>();
        try {
            file(filepath, 0, 0, list);
            for (Tree tr:list) {
                tr.setPath(tr.getPath().substring(Main.root.length()));
            }
            System.out.println(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static void file(String filepath, int parentid, int id, List<Tree> list) throws FileNotFoundException {
        File file = new File(filepath);
        //1.判断文件
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        String name = file.getName();
        String path = file.getPath();
        Tree tree = new Tree(id++, name, path, parentid);
        tree.setLength(file.length());
        tree.setLastModify(file.lastModified());
        //2.是文件该怎么执行
        if (file.isFile()) {
            tree.setLeft(true);
            tree.setLength(file.length());
            list.add(tree);
            return;
        }
        //3.获取文件夹路径下面的所有文件递归调用；
        if (file.isDirectory()) {
            tree.setLeft(false);
            list.add(tree);
            String[] ls = file.list();
            for (int i = 0; i < ls.length; i++) {
                String s = ls[i];
                String newFilePath = path + "/" + s;//根据当前文件夹，拼接其下文文件形成新的路径
                file(newFilePath, tree.getId(), id++, list);
            }
        }
    }
}