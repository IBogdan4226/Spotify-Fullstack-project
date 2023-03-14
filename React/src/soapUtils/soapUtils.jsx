import xml2js from 'xml2js';
import axios from 'axios';


export const login = (user, password, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
      <s11:Body>
        <ns1:login xmlns:ns1='services.identityManager.soap'>
          <ns1:username>${user}</ns1:username>
          <ns1:password>${password}</ns1:password>
       </ns1:login>
       </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                } else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:loginResponse'][0]['tns:loginResult'][0];
                    callback(output);
                }
            });
        })
        .catch(err => {
            throw err;
        })
}



export const logout = (token, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:logout xmlns:ns1='services.identityManager.soap'>
                 <ns1:jwtToVerify>${token}</ns1:jwtToVerify>
            </ns1:logout>
         </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                }
                else {
                    callback();
                    console.log(responseText)
                }
            });
        })
        .catch(err => {
            throw err;
        })
}


export const createAccount = (user, password, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:create_user xmlns:ns1='services.identityManager.soap'>
                <ns1:username>${user}</ns1:username>
                <ns1:password>${password}</ns1:password>
            </ns1:create_user>
        </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                }
                else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:create_userResponse'][0]['tns:create_userResult'][0];
                    addRoleToUser(output, 1, 'clientUser', () => { });
                    callback(output);
                }
            });
        })
        .catch(err => {
            throw err;
        })
}

export const addRoleToUser = (userId, roleId, token, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:add_role_to_user xmlns:ns1='services.identityManager.soap'>
                <ns1:userId>${userId}</ns1:userId>
                <ns1:roleId>${roleId}</ns1:roleId>
                <ns1:token>${token}</ns1:token>
            </ns1:add_role_to_user>
        </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                }
                else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:add_role_to_userResponse'][0]['tns:add_role_to_userResult'][0];
                    console.log("Add role to user " + output);
                    callback();
                }
            });
        })
        .catch(err => {
            console.log(err);
        })
}



export const authorize = (token, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:authorization xmlns:ns1='services.identityManager.soap'>
                <ns1:jwtToVerify>${token}</ns1:jwtToVerify>
            </ns1:authorization>
        </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                    callback("");
                }
                else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:authorizationResponse'][0]['tns:authorizationResult'][0];
                    if (output === "Token invalid") {
                        callback("")
                    }
                    else {
                        callback(output);
                    }
                    console.log(responseText)
                }
            });
        })
        .catch(err => {
            console.error(err);
            callback("");
        })
}

export const getUsers = (token, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:get_users xmlns:ns1='services.identityManager.soap'>
                <ns1:token>${token}</ns1:token>
        </ns1:get_users>
        </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                    callback("");
                }
                else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:get_usersResponse'][0]['tns:get_usersResult'][0];
                    console.log(output);
                    if (output === "Invalid token" || output === "Unathorized") {
                        callback("")
                    }
                    else {
                        callback(output);
                    }
                }
            });
        })
        .catch(err => {
            console.error(err);
            callback("");
        })
}

export const makeClientArtist = (userId, token, callback) => {
    let xmls = `
    <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
        <s11:Body>
            <ns1:add_role_to_user xmlns:ns1='services.identityManager.soap'>
                <ns1:userId>${userId}</ns1:userId>
                <ns1:roleId>2</ns1:roleId>
                <ns1:token>${token}</ns1:token>
            </ns1:add_role_to_user>
        </s11:Body>
    </s11:Envelope>`

    axios.post(process.env.REACT_APP_AUTH_SOAP,
        xmls,
        {
            headers:
            {
                'Content-Type': 'text/xml',
            }
        }).then(res => res.data)
        .then(responseText => {
            xml2js.parseString(responseText, (err, result) => {
                if (err) {
                    console.error(err);
                    callback(false);
                }
                else {
                    const output = result['soap11env:Envelope']['soap11env:Body'][0]['tns:add_role_to_userResponse'][0]['tns:add_role_to_userResult'][0];
                    console.log(output);
                    if(output==="True"){
                        callback(true);
                    }
                    else{
                        callback(false);
                    }
                }
            });
        })
        .catch(err => {
            console.error(err);
            callback(false);
        })
}