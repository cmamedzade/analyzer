package infra

import akka.stream.scaladsl.{FileIO, Framing, Source}
import akka.util.ByteString

import java.io.File

object FileHandler {

  def listDirectory(path: String): Array[File] = {
    val fileIO = new File(path)
    if (fileIO.isDirectory && fileIO.exists() ){
      fileIO.listFiles(_.isFile)
    }
    else{
      Array.empty[File]
    }
  }

  def readFromFiles(path: String) = {
    Source
      .fromIterator(() => listDirectory(path).iterator)
      .flatMapConcat { path =>
        FileIO
          .fromPath(path.toPath)
          .via(Framing.delimiter(ByteString("\n"), 256, true))
          .map(_.utf8String)
      }
  }
}
