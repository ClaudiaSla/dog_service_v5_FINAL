const express = require("express")
const mysql = require("mysql2")
const bodyParser = require("body-parser")
const req = require("express/lib/request")
const res = require("express/lib/response")
const bcrypt = require("bcryptjs"); // Importar bcryptjs PARA PODER ENCRIPTAR LA CONTRASEÑA
const nodemailer = require("nodemailer"); //ENVIO DE CORREOS.
const crypto = require("crypto"); // Importa el módulo crypto
const fs = require('fs'); // Importa el módulo fs para leer archivos


const app =  express()
app.use(bodyParser.json())

// Servir archivos estáticos desde la carpeta 'public'-PARA PARA CARGAR LA IMAGEN EN EL SERVIDOR WEB Y ME MUESTRE EN EL HTML
app.use(express.static('public'));


const PUERTO = 3000
const saltRounds = 10; // Se utiliza para ajustar la cantidad de iteraciones que el algoritmo de hash (en este caso, bcrypt) ejecutará para generar el hash de la contraseña.

const conexion = mysql.createConnection(
    {
        host:'localhost', 
        database: 'perrosapp',
        user: 'root',
        password:'root',
        
    }
)

app.listen(PUERTO, ()=> {
    console.log("Servidor corriendo en el puerto "+PUERTO);
})

conexion.connect(error => {
    if (error) {
        console.error("Error al conectar a la base de datos:", error);
        throw error;
    }
    console.log("Conexión a base de datos exitosa");
})

app.get("/", (req, res)=> {
    res.send("Servicio corriendo")
})

// Ruta POST para mostrar los Usuarios Registrados
app.get("/usuarios", (req, res)=> {
    const query = "SELECT * FROM Usuarios;"
    conexion.query(query, (error, resultado)=> {

        if(error){
            return console.error(error.message)
        } 

            const objeto = {}
            if(resultado.length > 0){
                objeto.listaUsuarios  = resultado
                res.json(objeto)
            }else{
                res.json("No hay registros")
            }
    })
})
app.get("/avistamientos", (req, res)=> {
    const query = "SELECT * FROM avistamiento;"
    conexion.query(query, (error, resultado)=> {

        if(error){
            return console.error(error.message)
        } 
            const objeto = {}
           
            if(resultado.length > 0){
                objeto.listaAvistamientos  = resultado
                res.json(objeto)
            }else{
                res.json("No hay objeto")
            }
    })
})
app.get("/adopcion", (req, res)=> {
    const query = "SELECT * FROM adopcion;"
    conexion.query(query, (error, resultado)=> {

        if(error){
            return console.error(error.message)
        } 

            const objeto = {}
            if(resultado.length > 0){
                objeto.listaAdopcion  = resultado
                res.json(objeto)
            }else{
                res.json("No hay registro")
            }
    })
})

app.get("/publicacion/:id",(req,res)=>{
    const {id}=req.params
    const query ='SELECT * FROM publicacion where id_publicacion ='+id 
    conexion.query(query,(error,resultado)=>{ 
      if(error) 
        return console.error(error.message)
      if (resultado.length > 0){
        res.json(resultado[0]) 
      }else{
        res.json("No hay registros") 
      }
    }) 
  })

app.post("/publicacion/agregar",(req,res)=>{
    const publicacion={
        publicacion_titulo:req.body.publicacion_titulo,
        publicacion_descripcion:req.body.publicacion_descripcion,
        publicacion_fecha:req.body.publicacion_fecha,
        Usuarios_id_usuarios:req.body.Usuarios_id_usuarios
    }
    const query = "INSERT INTO publicacion SET ?"
    conexion.query(query, publicacion, (error,result) =>{
        if(error) return console.error(error.message)
        res.json({id_publicacion: result.insertId})
    })
})
  app.post("/avistamiento/agregar",(req,res)=>{
    const avistamiento={
        avistamiento_fecha:req.body.avistamiento_fecha,
        avistamiento_lugar:req.body.avistamiento_lugar,
        Publicacion_id_publicacion:req.body.Publicacion_id_publicacion,
        Ubicacion_ubicacion_id:req.body.Ubicacion_ubicacion_id
    }
    const query = "INSERT INTO avistamiento SET ?"
    conexion.query(query, avistamiento, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el avistamiento")
    })
})
app.post("/adopcion/agregar",(req,res)=>{
    const adopcion={
        adopcion_edad:req.body.adopcion_edad,
        adopcion_raza:req.body.adopcion_raza,
        adopcion_vacunas:req.body.adopcion_vacunas,
        adopcion_nombre:req.body.adopcion_nombre,
        adopcion_lugar:req.body.adopcion_lugar,
        Publicacion_id_publicacion:req.body.Publicacion_id_publicacion,
        Ubicacion_ubicacion_id:req.body.Ubicacion_ubicacion_id
    }
    const query = "INSERT INTO adopcion SET ?"
    conexion.query(query, adopcion, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente la adopcion")
    })
})

// Función para encriptar la contraseña con BCrypt
function encriptarContrasena(contrasena) {
    return bcrypt.hashSync(contrasena, saltRounds);
  }
  
// Ruta POST para registrar un nuevo usuario
app.post("/registrar", (req, res) => {
    const { correo, nombre, apellido, contrasena, numero ,estado} = req.body;

    // Verificar que estado tenga un valor asignado
    if (!estado) {
        res.status(400).send("El campo 'estado' es obligatorio");
        return;
    }

    const contrasenaEncriptada = encriptarContrasena(contrasena); // Encriptar la contraseña
    const query = "INSERT INTO Usuarios (correo, nombre, apellido, contrasena, numero,Estado_id_estado) VALUES (?, ?, ?, ?, ?, ?)";
    conexion.query(query, [correo, nombre, apellido, contrasenaEncriptada, numero,estado], (error, resultado) => {
        if (error) {
            console.error("Error al insertar usuario:", error);
            res.status(500).send("Error al insertar usuario");
            return;
        }

         // Leer el archivo HTML y reemplazar los placeholders
         fs.readFile('public/registroExitoso.html', 'utf8', (err, html) => {
            if (err) {
                console.error("Error al leer el archivo HTML:", err);
                res.status(500).send("Error interno del servidor");
                return;
            }
             // Reemplazar los placeholders con los valores reales
             const htmlConDatos = html.replace('{{nombre}}', nombre).replace('{{apellido}}', apellido)
                                      .replace('{{correo}}', correo);


            // Envío de correo de confirmación
            const transporter = nodemailer.createTransport({
                service: 'gmail',
                auth: {
                    user: 'aplicacionesmovilesdesarrollo@gmail.com',
                    pass: 'g x n b n l r c r s pad z n v​ ​' // Usar contraseña de aplicación si es necesario
                }
            });

            const mailOptions = {
                from: 'aplicacionesmovilesdesarrollo@gmail.com',
                to: correo,
                subject: 'Registro Exitoso',
                html: htmlConDatos,
                attachments: [
                    {
                        filename: 'perro.png',
                        path: 'public/perro.png',
                        cid: 'logo'
                    }
                ]
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.error("Error al enviar el correo de confirmación:", error);
                    return res.status(500).json({ message: "Error al enviar el correo de confirmación" });
                }
                console.log('Correo de confirmación enviado: ' + info.response);

                res.status(201).send("Usuario registrado exitosamente");
            });
        });

    });
});


// Ruta POST para iniciar sesión
app.post('/login', (req, res) => {
    const { correo, contrasena } = req.body;

    // Consulta SQL para obtener el usuario por correo
    const sql = 'SELECT * FROM Usuarios WHERE correo = ?';
    conexion.query(sql, [correo], (err, results) => {
        if (err) {
            console.error("Error al realizar la consulta:", err);
            return res.status(500).json({ message: "Error en el servidor" });
        }

        // Verificar si se encontró un usuario con el correo proporcionado
        if (results.length === 0) {
            return res.status(401).json({ message: "Credenciales incorrectas" });
        }

        // Comparar la contraseña proporcionada con la almacenada en la base de datos
        const usuario = results[0];
        bcrypt.compare(contrasena, usuario.contrasena, (bcryptErr, bcryptResult) => {
            if (bcryptErr) {
                console.error("Error al comparar contraseñas:", bcryptErr);
                return res.status(500).json({ message: "Error en el servidor" });
            }

            if (!bcryptResult) {
                return res.status(401).json({ message: "Credenciales incorrectas" });
            }

            // Inicio de sesión exitoso
            res.status(200).json(usuario);
        });
    });
});

// Ruta POST para solicitud envio de TOKEN porque Olvido Contraseña
app.post('/olvidaste-contrasena', (req, res) => {
    const { correo} = req.body;
    const token = crypto.randomBytes(2).toString('hex');
    const query = "UPDATE Usuarios SET token_restablecimiento = ? WHERE correo = ?";
    conexion.query(query, [token, correo], (error, resultado) => {
        if (error) {
            console.error("Error al guardar el token:", error);
            res.status(500).send("Error al procesar la solicitud");
            return;
        }
        if (resultado.affectedRows === 0) {
            return res.status(404).send("Correo no registrado");
        }

         // Leer el archivo HTML y reemplazar los placeholders
     fs.readFile('public/olvidasteContraseña.html', 'utf8', (err, html) => {
        if (err) {
            console.error("Error al leer el archivo HTML:", err);
            res.status(500).send("Error interno del servidor");
            return;
        }
         // Reemplazar los placeholders con los valores reales
         const htmlConDatos = html.replace('{{correo}}', correo).
                                   replace('{{token}}', token);


            const transporter = nodemailer.createTransport({
                service: 'gmail',
                auth: {
                    user: 'aplicacionesmovilesdesarrollo@gmail.com',
                    pass: 'g x n b n l r c r s pad z n v​ ​'  // Usar contraseña de aplicación si es necesario
                }
            });

            const mailOptions = {
                from: 'aplicacionesmovilesdesarrollo@gmail.com',
                to: correo,
                subject: 'Token: Restablecer Contraseña',
                html: htmlConDatos,
                attachments: [
                    {
                        filename: 'perro.png',
                        path: 'public/perro.png',
                        cid: 'logo'
                    }
                ]
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.error("Error al enviar el correo:", error);
                    return res.status(500).json({ message: "Error al enviar el correo" });
                }
                console.log('Correo enviado: ' + info.response);
                res.status(200).json({ message: "Correo enviado con éxito" });
            });
        });
    });
});

// Ruta POST para validar el token de restablecimiento de contraseña
app.post('/valida-token', (req, res) => {
    const { correo, token } = req.body;
    // Consultar en la base de datos si el correo y el token son válidos
    const query = "SELECT * FROM Usuarios WHERE correo = ? AND token_restablecimiento = ?";
    conexion.query(query, [correo, token], (error, results) => {
        if (error) {
            console.error("Error al verificar el token:", error);
            return res.status(500).json({ message: "Error al procesar la solicitud" });
        }
        if (results.length === 0) {
            return res.status(400).json({ message: "Token inválido o expirado" });
        }
        // El token es válido, responder con éxito
        res.status(200).json({ message: "Token validado correctamente" });
    });
});

// Ruta POST para reestablecer la contraseña
app.post('/restablecer-contrasena', (req, res) => {
    const { correo, nuevaContrasena} = req.body;


    console.log("Correo:", correo);
    console.log("Nueva Contraseña:", nuevaContrasena);
    
    if (typeof nuevaContrasena !== 'string' || nuevaContrasena.trim().length === 0) {
        return res.status(400).json({ message: "La nueva contraseña es requerida y debe ser una cadena no vacía" });
    }

     // Leer el archivo HTML y reemplazar los placeholders
     fs.readFile('public/restablecerContraseña.html', 'utf8', (err, html) => {
        if (err) {
            console.error("Error al leer el archivo HTML:", err);
            res.status(500).send("Error interno del servidor");
            return;
        }
         // Reemplazar los placeholders con los valores reales
         const htmlConDatos = html.replace('{{correo}}', correo)
                                  .replace('{{contrasenaEncriptada}}', nuevaContrasena);


        // Encriptar la nueva contraseña antes de guardarla
        const contrasenaEncriptada = encriptarContrasena(nuevaContrasena);

        // Actualizar la contraseña en la base de datos
        const updateQuery = "UPDATE Usuarios SET contrasena = ?, token_restablecimiento = NULL WHERE correo = ?";
        conexion.query(updateQuery, [contrasenaEncriptada, correo], (updateError, result) => {
            if (updateError) {
                console.error("Error al actualizar la contraseña:", updateError);
                return res.status(500).json({ message: "Error al procesar la solicitud" });
            }
            if (result.affectedRows === 0) {
                return res.status(404).json({ message: "Correo no registrado" });
            }
            // Envío de correo de confirmación
            const transporter = nodemailer.createTransport({
                service: 'gmail',
                auth: {
                    user: 'aplicacionesmovilesdesarrollo@gmail.com',
                    pass: 'g x n b n l r c r s pad z n v​ ​'  // Usar contraseña de aplicación si es necesario
                }
            });

            const mailOptions = {
                from: 'aplicacionesmovilesdesarrollo@gmail.com',
                to: correo,
                subject: 'Restablecimiento de Contraseña Exitoso',
                html: htmlConDatos,
                attachments: [
                    {
                        filename: 'perro.png',
                        path: 'public/perro.png',
                        cid: 'logo'
                    }
                ]
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.error("Error al enviar el correo de confirmación:", error);
                    return res.status(500).json({ message: "Error al enviar el correo de confirmación" });
                }
                console.log('Correo de confirmación enviado: ' + info.response);
                res.status(200).json({ message: "Contraseña restablecida exitosamente" });
            });
        });
        
    });
});



//PERDIDOS
//REGISTRO MASCOTA PERDIDA
app.post("/perdido/agregar",(req,res)=>{
    const perdido={
        perdido_fecha:req.body.perdido_fecha,
        perdido_lugar:req.body.perdido_lugar,
        perdido_contacto:req.body.perdido_contacto,
        Publicacion_id_publicacion:req.body.Publicacion_id_publicacion,
        Ubicacion_ubicacion_id:req.body.Ubicacion_ubicacion_id
    }
    const query = "INSERT INTO perdido SET ?"
    conexion.query(query, perdido, (error) =>{
        if(error) return console.error(error.message)
        res.json("Se insertó correctamente el avistamiento")
    })
})

//Perdido todos
app.get("/perdido/obtener", (req, res) => {
    const query = "SELECT * FROM perdido";
    conexion.query(query, (error, results) => {
        if (error) {
            return console.error(error.message);
        }
        res.json(results);
    });
});

//Perdido consulta 1
app.get("/perdido/obtener/:id", (req, res) => {
    const id = req.params.id;
    const query = `
        SELECT 
            usuarios.nombre,
            publicacion.publicacion_fecha,
            publicacion.publicacion_descripcion,
            publicacion.publicacion_titulo,
            perdido.perdido_fecha,
            perdido.perdido_lugar,
            perdido.perdido_contacto
        FROM 
            perdido
        JOIN 
            publicacion ON perdido.publicacion_id_publicacion = publicacion.id_publicacion
        JOIN 
            usuarios ON Publicacion.usuarios_id_usuarios = usuarios.id_usuarios
        WHERE 
            perdido.perdido_id = ?
    `;
    conexion.query(query, [id], (error, resultado) => {
        if (error) {
            return console.error(error.message);
        }

        if(resultado.length > 0){
            res.json(resultado[0])
        }else{
            res.json("No hay registro")
        }
    });
});

//Imagen perdido
app.post("/upload_image", (req, res) => {
    const { idImage, nomImage, image, Publicacion_id_publication } = req.body;
    const path = `img/${nomImage}.png`;
    const actualPath = `http://localhost:${PORT}/img/${nomImage}.png`;

    fs.writeFile(path, Buffer.from(image, 'base64'), (err) => {
        if (err) {
            console.error('Error saving the image:', err);
            res.status(500).json("OCURRIO UN ERROR");
            return;
        }

        const query = "INSERT INTO imagenes (imagenes_id, imagenes_url, Publicacion_id_publication) VALUES (?, ?, ?)";
        conexion.query(query, [idImage, actualPath, Publicacion_id_publication], (error) => {
            if (error) {
                console.error('Error inserting into the database:', error);
                res.status(500).json("OCURRIO UN ERROR");
                return;
            }
            res.json("SE INSERTO CORRECTAMENTE LA FOTO");
        });
    });
});

app.get("/perdido", (req, res) => {
    const query = `
        SELECT p.publicacion_titulo, perdido.perdido_lugar, perdido.perdido_fecha, p.Usuarios_id_usuarios, p.publicacion_fecha, perdido.perdido_contacto, p.publicacion_descripcion
        FROM perdido
        INNER JOIN publicacion p ON Publicacion_id_publicacion = p.id_publicacion;
    `;
    
    conexion.query(query, (error, resultado) => {
        if (error) {
            return console.error(error.message);
        }

        const objeto = {};
        if (resultado.length > 0) {
            objeto.listaPerdidos = resultado;
            res.json(objeto);
        } else {
            res.json("No hay avistamientos registrados");
        }
    });
});
