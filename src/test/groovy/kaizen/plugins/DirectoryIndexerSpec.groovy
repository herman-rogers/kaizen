package kaizen.plugins

import spock.lang.Specification

class DirectoryIndexerSpec extends Specification {

	def repositoryDir = DirectoryBuilder.tempDirWith {
		dir('d1') {
			dir('d2') {
			}
		}
		file('f1')
	}

	void setup() {
		DirectoryIndexer.index(repositoryDir)
	}

	def 'index.html is generated for each directory'() {
		expect:
		file(repositoryDir, 'index.html').exists()
		file(repositoryDir, 'd1/index.html').exists()
		file(repositoryDir, 'd1/d2/index.html').exists()
	}

	def 'index.html contains links to each file and directory'() {
		def expected = '''
<html>
	<body>
		<h1>Directory listing</h1>
		<hr />
		<pre>
			<a href='d1'>d1</a>
			<a href='f1'>f1</a>
		</pre>
	</body>
</html>
'''
		expect:
		file(repositoryDir, 'index.html').text == expected.trim().replace("\t", "  ")
	}

	File file(File parent, String path) {
		new File(parent, path)
	}
}