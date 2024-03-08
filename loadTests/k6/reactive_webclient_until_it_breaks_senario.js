import http from "k6/http";
import {check, sleep} from "k6";

export const options = {
    stages: [
        { duration: "10s", target: 5 },
        { duration: "10s", target: 10 },
        { duration: "10s", target: 20 },
        { duration: "10s", target: 40 },
        { duration: "10s", target: 80 },
        { duration: "10s", target: 160 },
        { duration: "10s", target: 320 },
        { duration: "10s", target: 640 },
        { duration: "10s", target: 1280 },
        { duration: "10s", target: 2560 },
        { duration: "10s", target: 5120 },
        { duration: "10s", target: 10240 },
    ],
};

export default function() {
    let res = http.get( "http://localhost:8080/reactiveWebClient/1" );

    check( res, {
        "status is 200": (r) => r.status === 200,
    });

    sleep(1);
}