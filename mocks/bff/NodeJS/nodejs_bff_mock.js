const http = require( "http" )

const host = '127.0.0.1'
const port = 8082

const DEFAULT_ANSWER = JSON.stringify( { key: "param" } )


const requestListener = function (req, res) {
    res.writeHead( 200 )
    res.end( DEFAULT_ANSWER )
}

const server = http.createServer( requestListener );

server.listen( port, host, () =>
    console.log( `Server is running on http://${host}:${port}` )
)
