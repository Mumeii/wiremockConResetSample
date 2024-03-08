import http from "k6/http";
import {check, sleep} from "k6";

export const options = {
    stages: [
        { duration: "20s", target: 300 },
        { duration: "50s", target: 300 }
    ],
};

export default function() {
    let res = http.get( "http://localhost:8080/nonReactive/1" );

    check( res, {
        "status is 200": (r) => r.status === 200,
    });

    sleep(1);
}