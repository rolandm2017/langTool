import { Transform } from 'stream'

class LineTransform extends Transform {
    constructor(options) {
        super(options)
        this.buffer = ''
    }

    _transform(chunk, encoding, callback) {
        this.buffer += chunk.toString()
        let lines = this.buffer.split('\n')
        this.buffer = lines.pop() // Keep the last partial line in the buffer

        for (let line of lines) {
            this.push(line)
        }

        callback()
    }

    _flush(callback) {
        if (this.buffer) {
            this.push(this.buffer)
        }
        callback()
    }
}

export { LineTransform }
