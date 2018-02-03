const koa = require('koa');
const app = new koa();
const server = require('http').createServer(app.callback());
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyParser = require('koa-bodyparser');
const convert = require('koa-convert');

app.use(bodyParser());
app.use(convert(cors()));
app.use(async(ctx, next) => {
    const start = new Date();
    await next();
    const ms = new Date() - start;
    console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

const getRandomInt = (min, max) => {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
};

const ideaSampleNames = ['The Good', 'The Bad', 'The Ugly'];
const ideaTypes = ['small', 'medium', 'large'];
const statusTypes = ['idea', 'pending', 'approved', 'discarded'];
const projects = [];
for (let i = 0; i < 5; i++) {
    projects.push({
        id: i + 1,
        name: ideaSampleNames[getRandomInt(0, ideaSampleNames.length - 1)] + " " + (i + 1),
        type: ideaTypes[getRandomInt(0, ideaTypes.length - 1)],
        status: statusTypes[0],
        budget: getRandomInt(1, 5)
    });
}
for (let i = 5; i < 10; i++) {
    projects.push({
        id: i + 1,
        name: ideaSampleNames[getRandomInt(0, ideaSampleNames.length - 1)] + " " + (i + 1),
        type: ideaTypes[getRandomInt(0, ideaTypes.length - 1)],
        status: statusTypes[1],
        budget: getRandomInt(1, 5)
    });
}

const router = new Router();
router.get('/ideas', ctx => {
    ctx.response.body = projects.filter(project => project.status === statusTypes[0]);
    ctx.response.status = 200;
});

router.get('/projects', ctx => {
    ctx.response.body = projects.filter(project => project.status !== statusTypes[0]);
    ctx.response.status = 200;
});

router.post('/add', ctx => {
    const headers = ctx.request.body;
    console.log("body: " + JSON.stringify(headers));
    const name = headers.name;
    const type = headers.type;
    const budget = headers.budget;
    if (typeof name !== 'undefined' && typeof type !== 'undefined' && typeof budget !== 'undefined') {
        const index = projects.findIndex(idea => idea.name === name && idea.type === type);
        if (index === -1) {
            let maxId = Math.max.apply(Math, projects.map(function (idea) {
                    return idea.id;
                })) + 1;
            let idea = {
                id: maxId,
                name,
                type,
                budget,
                status: statusTypes[0]
            };
            projects.push(idea);
            ctx.response.body = idea;
            ctx.response.status = 200;
        } else {
            ctx.response.body = {text: 'Idea exists already!'};
            ctx.response.status = 404;
        }
    } else {
        ctx.response.body = {text: 'Missing name or type or budget'};
        ctx.response.status = 404;
    }
});

router.del('/delete/:id', ctx => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.params;
    console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== 'undefined') {
        const index = projects.findIndex(project => project.id == id);
        if (index === -1) {
            ctx.response.body = {text: 'No such idea'};
            ctx.response.status = 404;

        } else {
            let idea = projects[index];
            projects.splice(index, 1);
            ctx.response.body = idea;
            ctx.response.status = 200;
        }
    }
    else {
        ctx.response.body = {text: 'Missing or invalid id'};
        ctx.response.status = 404;
    }
});


router.post('/promote', ctx => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.request.body;
    console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== 'undefined') {
        const index = projects.findIndex(idea => idea.id == id);
        if (index === -1) {
            console.log("No idea with id: " + id);
            ctx.response.body = {text: 'Invalid id, or missing!'};
            ctx.response.status = 404;
        } else {
            let idea = projects[index];
            if (idea.status !== statusTypes[0]) {
                ctx.response.body = {text: 'Already a project'};
                ctx.response.status = 404;
            } else {
                idea.status = statusTypes[1];
                ctx.response.body = idea;
                ctx.response.status = 200;
            }
        }
    } else {
        ctx.response.body = {text: 'Id missing'};
        ctx.response.status = 404;
    }
});


router.post('/approve', ctx => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.request.body;
    console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== 'undefined') {
        const index = projects.findIndex(project => project.id == id);
        if (index === -1) {
            console.log("No project with id: " + id);
            ctx.response.body = {text: 'Invalid id, or missing!'};
            ctx.response.status = 404;
        } else {
            let project = projects[index];
            if (project.status !== statusTypes[1]) {
                ctx.response.body = {text: 'Project not in pending status'};
                ctx.response.status = 404;
            } else {
                project.status = statusTypes[2];
                ctx.response.body = project;
                ctx.response.status = 200;
            }
        }
    } else {
        ctx.response.body = {text: 'Id missing'};
        ctx.response.status = 404;
    }
});


router.post('/discard', ctx => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.request.body;
    console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== 'undefined') {
        const index = projects.findIndex(project => project.id == id);
        if (index === -1) {
            console.log("No project with id: " + id);
            ctx.response.body = {text: 'Invalid id, or missing!'};
            ctx.response.status = 404;
        } else {
            let project = projects[index];
            if (project.status === statusTypes[0]) {
                ctx.response.body = {text: 'Not able to discard ideas yet'};
                ctx.response.status = 404;
            } else {
                project.status = statusTypes[3];
                ctx.response.body = project;
                ctx.response.status = 200;
            }
        }
    } else {
        ctx.response.body = {text: 'Id missing'};
        ctx.response.status = 404;
    }
});


router.del('/remove/:id', ctx => {
    // console.log("ctx: " + JSON.stringify(ctx));
    const headers = ctx.params;
    console.log("body: " + JSON.stringify(headers));
    const id = headers.id;
    if (typeof id !== 'undefined') {
        const index = projects.findIndex(project => project.id == id);
        if (index === -1) {
            console.log("No project with id: " + id);
            ctx.response.body = {text: 'Invalid id'};
            ctx.response.status = 404;
        } else {
            let project = projects[index];
            if (project.status === statusTypes[0]) {
                ctx.response.body = {text: 'Not able to remove ideas!'};
                ctx.response.status = 404;
            } else {
                let project = projects[index];
                projects.splice(index, 1);
                ctx.response.body = project;
                ctx.response.status = 200;
            }
        }
    } else {
        ctx.response.body = {text: 'Id missing'};
        ctx.response.status = 404;
    }
});


app.use(router.routes());
app.use(router.allowedMethods());

server.listen(4026);