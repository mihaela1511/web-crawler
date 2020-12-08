package ro.mta.facc.webcrawler.filter;

import ro.mta.facc.webcrawler.config.WebCrawlerConfig;

/**
 * Aceasta clasa se ocupa de filtrarea fisierelor dintr-un anumit director in functie de o lista de cuvinte cheie
 */
public class KeywordFilter {

        private void emptyDirectory(File dir){

        for (File file : dir.listFiles()){
            if (file.isDirectory())
            {
                emptyDirectory(file);
            }
            file.delete();
        }
    }

    private void emptyDefaultDir(String root){

        File dir = new File(Paths.get(root,"KeywordFilteredFiles").toString());

        if (!dir.exists()){
            return;
        }

        emptyDirectory(dir);

        dir.delete();
    }


    private void moveFilteredFiles(List<Path> filteredFiles, String baseDir) throws IOException {

        final Path filteredDirPath = Path.of(baseDir,"KeywordFilteredFiles");

        if (!Files.exists(filteredDirPath)) {
            Files.createDirectory(filteredDirPath);
        }

        filteredFiles.forEach(filePath->{

            String absPath = filePath.toAbsolutePath().toString();

            absPath = absPath.replace(baseDir,filteredDirPath.toString());

                try {
                Path p = Paths.get(absPath);


                if (!Files.exists(p.getParent())){
                    new File(p.toString()).mkdirs();
                }

                Files.copy(filePath,p, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    
    
    public static void filter(String directoryPath, WebCrawlerConfig crawlerConfig) {
 emptyDefaultDir(directoryPath);

        final List<String> keywords = crawlerConfig.getKeywords();

        Path startPath = Path.of(directoryPath);

        List<Path> filteredFiles = Files.find(startPath,99,(path, attrs)->{

            try {
                if (Files.isDirectory(path)){
                    return false;
                }
                List<String> lines = Files.readAllLines(path);

                String text = String.join("\n", lines);

                lines.clear();

                return keywords.stream().anyMatch(text::contains);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());


        moveFilteredFiles(filteredFiles,directoryPath);
    }
}
