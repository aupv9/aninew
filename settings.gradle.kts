rootProject.name = "front-api"



val whiteListFolderSubProject = listOf("common", "front-api", "back-api", "core", "cloud-storage", "audit")
rootDir.listFiles()?.filter {
    whiteListFolderSubProject.contains(it.name)
}?.map { include(it.name) }
